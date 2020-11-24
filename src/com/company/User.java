package com.company;

import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.*;

import java.net.URL;
import java.nio.file.Paths;

public class User extends NullFederateAmbassador {
    private RtiFactory rtiFactory;
    private EncoderFactory encoderFactory;
    private RTIambassador rtIambassador;
    private HLAunicodeString hlAunicodeString;
    private URL formModule;
    private String formmoduleName;
    private String federationExecutionName;
    private String federationType;
    private InteractionClassHandle handle;

    public User(){
        formmoduleName = "Chatroom.xml";

        federationExecutionName = "chatroom-federation";
        federationType = "User";

    }

    void init() throws Exception {

        rtiFactory = RtiFactoryFactory.getRtiFactory();
        encoderFactory = rtiFactory.getEncoderFactory();
        rtIambassador = rtiFactory.getRtiAmbassador();
        rtIambassador.connect(this, CallbackModel.HLA_IMMEDIATE);
        formModule = Paths.get(formmoduleName).toUri().toURL();

        try{
            rtIambassador.createFederationExecution(federationExecutionName, formModule);

        } catch(FederationExecutionAlreadyExists e){
            System.out.println("Failed");
        }
        rtIambassador.joinFederationExecution(federationType,federationExecutionName);

        handle = rtIambassador.getInteractionClassHandle("HLAinteractionRoot.Message");




    }

    public void publishInteractions() throws RestoreInProgress, InteractionClassNotDefined, SaveInProgress, FederateNotExecutionMember, RTIinternalError, NotConnected, NameNotFound, InvalidInteractionClassHandle, InteractionClassNotPublished, InteractionParameterNotDefined {

        rtIambassador.publishInteractionClass(handle);
        System.out.println("You can go");

    }

    public void sendInteraction(String text, String sender, String chatroom) throws RestoreInProgress, InteractionClassNotPublished, InteractionClassNotDefined, SaveInProgress, FederateNotExecutionMember, RTIinternalError, InteractionParameterNotDefined, NotConnected, NameNotFound, InvalidInteractionClassHandle {
        ParameterHandleValueMapFactory parameterHandleValueMapFactory;
        ParameterHandleValueMap parameterHandleValueMap;
        HLAunicodeString hLAunicodeStringText;
        HLAunicodeString hLAunicodeStringSender;
        HLAunicodeString hLAunicodeStringChatroom;

        hLAunicodeStringText = encoderFactory.createHLAunicodeString(text);
        hLAunicodeStringSender = encoderFactory.createHLAunicodeString(sender);
        hLAunicodeStringChatroom = encoderFactory.createHLAunicodeString(chatroom);

        parameterHandleValueMapFactory = rtIambassador.getParameterHandleValueMapFactory();
        parameterHandleValueMap = parameterHandleValueMapFactory.create(1024);

        parameterHandleValueMap.put(rtIambassador.getParameterHandle(handle,"Text"),hLAunicodeStringText.toByteArray());
        parameterHandleValueMap.put(rtIambassador.getParameterHandle(handle,"Sender"),hLAunicodeStringSender.toByteArray());
        parameterHandleValueMap.put(rtIambassador.getParameterHandle(handle,"Chatroom"),hLAunicodeStringChatroom.toByteArray());





        rtIambassador.sendInteraction(handle,parameterHandleValueMap,new byte[0]);
    }

    public void subscribeInteraction() throws NameNotFound, NotConnected, RTIinternalError, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, SaveInProgress, FederateServiceInvocationsAreBeingReportedViaMOM {

        rtIambassador.subscribeInteractionClass(handle);


    }

    @Override  public void receiveInteraction(InteractionClassHandle interactionClass,
                                              ParameterHandleValueMap theParameters,
                                              byte[] userSuppliedTag,
                                              OrderType sentOrdering,
                                              TransportationTypeHandle theTransport,
                                              SupplementalReceiveInfo receiveInfo)
            throws
            FederateInternalError{

        HLAunicodeString hLAunicodeStringReceivedText = encoderFactory.createHLAunicodeString();
        HLAunicodeString hLAunicodeStringReceivedSender = encoderFactory.createHLAunicodeString();
        HLAunicodeString hLAunicodeStringReceivedChatroom = encoderFactory.createHLAunicodeString();

        try {
            hLAunicodeStringReceivedText.decode(theParameters.get(rtIambassador.getParameterHandle(handle,"Text")));
            hLAunicodeStringReceivedSender.decode(theParameters.get(rtIambassador.getParameterHandle(handle,"Sender")));
            hLAunicodeStringReceivedChatroom.decode(theParameters.get(rtIambassador.getParameterHandle(handle,"Chatroom")));
            System.out.println();
            System.out.println(hLAunicodeStringReceivedSender.getValue() + " from"+ hLAunicodeStringReceivedChatroom.getValue() + ": " + hLAunicodeStringReceivedText.getValue());
        } catch (DecoderException | NameNotFound | InvalidInteractionClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            e.printStackTrace();
        }

    }
}

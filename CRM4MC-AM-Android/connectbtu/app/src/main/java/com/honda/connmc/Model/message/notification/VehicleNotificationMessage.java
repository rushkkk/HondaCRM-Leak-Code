package com.honda.connmc.Model.message.notification;

import com.honda.connmc.Constants.message.MsgId;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.security.VehicleParameterTransactionIdentity;
import com.honda.connmc.Utils.LogUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class VehicleNotificationMessage extends VehicleMessage {
    private static final int MAX_MSG_LENGTH = 240;
    public VehicleNotificationMessage(String message) {
        super(MsgId.VEHICLE_TRANSACTION_REQUEST);
        createDataMessage(message);
    }

    private void createDataMessage(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(message);

        //Check length data.
        if (builder.length() > MAX_MSG_LENGTH) {
            builder.delete(MAX_MSG_LENGTH - 2, builder.length());
            builder.append("…");
        }
        String content = builder.toString();
        LogUtils.d("Msg content: " + content);
        LogUtils.d("Msg length: " + content.length());


        byte[] contentByteArr = content.getBytes(StandardCharsets.UTF_8);
        final short MAX_LENGTH_DATA = 480;
        if (contentByteArr.length > MAX_LENGTH_DATA) {
            contentByteArr = Arrays.copyOfRange(contentByteArr.clone(),0, MAX_LENGTH_DATA - 1);
        }
        LogUtils.d("Msg content to byte arr length: " + contentByteArr.length);

        //Create Vehicle Transaction Request Message.
        int transactionIdentity = 0;
        addParams(new VehicleParameterTransactionIdentity(transactionIdentity));

        ByteBuffer bufferMpNameCommand = ByteBuffer.allocate(contentByteArr.length + 4);
        //=====create byte buffer data============
        bufferMpNameCommand.put((byte) content.length());
        byte code = (byte) (ParamValue.VehicleControl.MpNameCommand.CODE_UTF8);
        bufferMpNameCommand.put(code);
        short messageLength = (short) (contentByteArr.length);
        ByteBuffer byteBufferLength = ByteBuffer.allocate(2);
        byteBufferLength.putShort(messageLength);
        bufferMpNameCommand.put(byteBufferLength.array());
        bufferMpNameCommand.put(contentByteArr);
        //========================================
        /* Dummy data TAG 0x0013
        bufferMpNameCommand.put(new byte[]{03,01});
        ByteBuffer byteBufferLength = ByteBuffer.allocate(2);
        byteBufferLength.putShort((short) contentByteArr.length);
        bufferMpNameCommand.put(byteBufferLength.array());
        */
        LogUtils.d("param MP_NAME_COMMAND length: " + bufferMpNameCommand.array().length);

//        addParams(new VehicleParam(ParamTag.MP_NAME_COMMAND_TEST, bufferMpNameCommand.array()));
        addParams(new VehicleParam(ParamTag.MP_NAME_COMMAND, bufferMpNameCommand.array()));
    }
}


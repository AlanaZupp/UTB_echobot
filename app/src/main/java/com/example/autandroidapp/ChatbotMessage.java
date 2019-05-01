package com.example.autandroidapp;

public class ChatbotMessage
{
    private String msgText;
    private String msgUser;

    public ChatbotMessage(String msgText, String msgUser)
    {
        this.msgText = msgText;
        this.msgUser = msgUser;
    }

    public ChatbotMessage() { }

    public String getMsgText()
    {
        return msgText;
    }

    public void setMsgText(String msgText)
    {
        this.msgText = msgText;
    }

    public String getMsgUser()
    {
        return msgUser;
    }

    public void setMsgUser(String msgUser)
    {
        this.msgUser = msgUser;
    }
}

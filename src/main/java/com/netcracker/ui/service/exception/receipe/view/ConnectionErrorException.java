/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.exception.receipe.view;

/**
 *
 * @author Artem
 */
public class ConnectionErrorException extends ShortViewException{
    public ConnectionErrorException(){};
    public ConnectionErrorException(String msg){
        super(msg);
    };
    
    @Override
    public String toString()
    {
        return "Exception from IU-Service, ShortViewOfReceipe, Proxy";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.graf.component;

import com.netcracker.ui.service.graf.component.eventTypes.EventType;
import com.netcracker.ui.service.graf.component.events.EventListener;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;
import java.util.ArrayList;
import com.netcracker.ui.service.graf.component.events.addEdge.AddEdgeEvent;
import com.netcracker.ui.service.graf.component.events.addNode.AddNodeEvent;
import com.netcracker.ui.service.graf.component.events.clickOnNode.ClickOnNodeEvent;
import com.netcracker.ui.service.graf.component.events.deleteEdge.DeleteEdgeEvent;
import com.netcracker.ui.service.graf.component.events.deleteNode.DeleteNodeEvent;
import com.netcracker.ui.service.graf.component.events.editEdge.EditEdgeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Artem
 */
@JavaScript({"grafsLibrary.js", "graf-connector.js",
    "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js",
    "https://cdnjs.cloudflare.com/ajax/libs/vis/4.19.1/vis.js",
    "http://code.jquery.com/jquery-1.9.1.js"})
public class Graf extends AbstractJavaScriptComponent {
    
    //Массивы слушателей событий
    private ArrayList<EventListener> clickOnNodeListeners;
    private ArrayList<EventListener> addNodeListeners;
    private ArrayList<EventListener> addEdgeListeners;
    private ArrayList<EventListener> editEdgeListeners;
    private ArrayList<EventListener> deleteEdgeListeners;
    private ArrayList<EventListener> deleteNodeListeners;
    private ArrayList<EventListener> initNodeListeners;
    
    public Graf() {
        //Конфигурирование цепочки обработчиков событий
        DeleteNodeEvent deleteNodeEvent = new DeleteNodeEvent(this);
        DeleteEdgeEvent deleteEdgeEvent = new DeleteEdgeEvent(this);
        deleteEdgeEvent.setNext(deleteNodeEvent);
        EditEdgeEvent editEdgeEvent = new EditEdgeEvent(this);
        editEdgeEvent.setNext(deleteEdgeEvent);
        AddEdgeEvent addEdgeEvent = new AddEdgeEvent(this);
        addEdgeEvent.setNext(editEdgeEvent);
        AddNodeEvent addNodeEvent = new AddNodeEvent(this);
        addNodeEvent.setNext(addEdgeEvent);
        ClickOnNodeEvent firstPartCheinOfHandlersEvent = new ClickOnNodeEvent(this);
        firstPartCheinOfHandlersEvent.setNext(addNodeEvent);
        
        addFunction("onClick", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {                 
                firstPartCheinOfHandlersEvent.handleEvent(arguments);
            }
        });
    
        getState().nodes = new ArrayList<>();
        getState().edges = new ArrayList<>();
        clickOnNodeListeners = new ArrayList<>();
    }
    
    //Установка флага события, он обрабатывается на js и затем сбрасывается 
    //на строне js. Через эту функцию осуществляется манипулирование графом со 
    //стороны java
    public void setEvent(EventType event, String eventStateInJSONFormat)
    {
        getState().event = event.getType();
        getState().eventStateInJSONFormat = eventStateInJSONFormat;
    }
    
    public ArrayList<EventListener> getClickOnNodeListeners() {
        return clickOnNodeListeners;
    }

    public ArrayList<EventListener> getAddNodeListeners() {
        return addNodeListeners;
    }

    public ArrayList<EventListener> getAddEdgeListeners() {
        return addEdgeListeners;
    }

    public ArrayList<EventListener> getEditEdgeListeners() {
        return editEdgeListeners;
    }

    public ArrayList<EventListener> getDeleteEdgeListeners() {
        return deleteEdgeListeners;
    }

    public ArrayList<EventListener> getDeleteNodeListeners() {
        return deleteNodeListeners;
    }
    
    public ArrayList<EventListener> getInitListeners(){
        return initNodeListeners;
    }
    
    //Добавление ноды с обработчиком клика, вызывается
    //через изменение стейта со стороны js
    public void addNode(String newNodesimageUrl, String newNodesLabel, 
            String newNodesId, HandlerForClickingTheNode handler) {
        Node node = new Node(newNodesimageUrl, newNodesId, newNodesLabel);
        node.setHandlerForClickingTheNode(handler);
        getState().nodes.add(node);
    }
    
    //Добавление ноды, вызывается через изменение стейта со стороны js
    public void addNode(String newNodesimageUrl, String newNodesLabel, 
            String newNodesId) {
        Node node = new Node(newNodesimageUrl, newNodesId, newNodesLabel);
        getState().nodes.add(node);
    }
    
    //Добавление связи, вызывается через изменение стейта со стороны js
    public void addEdge(String idNodesConnectedFrom, 
            String idNodesConnectedTo) {
        Edge nodeConnection = new Edge(idNodesConnectedFrom, idNodesConnectedTo);
        getState().edges.add(nodeConnection);
    }
    
    //Редактирование связи, вызывается через изменение стейта со стороны js
    public void editEdge(String editableEdgesOldIdFrom, String editableEdgesOldIdTo, 
            String editableEdgesNewIdFrom, String editableEdgesNewIdTo){
        for(int i = 0; i< getState().edges.size();i++)
        {
            if(getState().edges.get(i).getStartNodeId() == editableEdgesOldIdFrom & 
                    getState().edges.get(i).getEndNodeId() == editableEdgesOldIdTo)
            {
                getState().edges.get(i).setStartNodeId(editableEdgesNewIdFrom);
                getState().edges.get(i).setEndNodeId(editableEdgesNewIdTo);
                break;
            }
        }
    }
    
    //Удаление связи, вызывается через изменение стейта со стороны js
    public void deleteEdge(String deleteEdgeFrom, String deleteEdgeTo){
        for(int i = 0; i< getState().edges.size();i++)
        {
            if(getState().edges.get(i).getStartNodeId() == deleteEdgeFrom & 
                    getState().edges.get(i).getEndNodeId() == deleteEdgeTo)
            {
                getState().edges.remove(i);
                setEvent(EventType.deleteEdge,"");
                break;
            }
        }
    }
    
    //Удаление ноды из стейта на стороне js после ее удаления с рабочего поля 
    //на стороне js, вызывается через изменение стейта со стороны js
    public void deleteNode(String deleteNodesId){
        for(int i = 0; i< getState().nodes.size();i++)
        {
            if(getState().nodes.get(i).getNodeId() == deleteNodesId)
            {
                //Удаление связей этой ноды
                for(int j=0; j<getState().edges.size();j++)
                {
                    if(getState().edges.get(j).getStartNodeId() == deleteNodesId | 
                            getState().edges.get(j).getEndNodeId() == deleteNodesId){
                        getState().edges.remove(j);
                    }
                }
                getState().nodes.remove(i);
                break;
            }         
        }
    }
    
    public void setInitCollections(ArrayList<Node> nodesCollection, 
            ArrayList<Edge> edgesCollection) {
        getState().nodes = nodesCollection;
        getState().edges = edgesCollection;
        setEvent(EventType.init,
                convertInitDataToRighFormat(nodesCollection, edgesCollection));
    }
    
    //Вернет строку в Json формате, с именами переменных, требуемых на 
    //стороне JS  
    private String convertInitDataToRighFormat(ArrayList<Node> nodesCollection, 
            ArrayList<Edge> edgesCollection)
    {
        JSONObject result =new JSONObject();
        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();
        
        for(int i=0; i<nodesCollection.size(); i++)
        {
            JSONObject node = new JSONObject();
            node.put("id", nodesCollection.get(i).getNodeId());
            node.put("label", "");
            node.put("shape", "circularImage");
            node.put("image", nodesCollection.get(i).getPictureId());
            nodes.put(i, node);
        }
        
        for(int i=0; i<edgesCollection.size(); i++)
        {
            JSONObject edge = new JSONObject();
            edge.put("from", edgesCollection.get(i).getStartNodeId());
            edge.put("to", edgesCollection.get(i).getEndNodeId());
            edge.put("arrows", "to");
            edges.put(i, edge);
        }
        
        result.put("nodes", nodes);
        result.put("edges", edges);
        
        return result.toString();
    }
    
    //Установка обработчика события клика по ноде на созданную ранее ноду
    public void setHandlerForClickingTheNode(String nodesId,
            HandlerForClickingTheNode handler) {
        for(int i=0; i<getState().nodes.size(); i++)
        {
            if(getState().nodes.get(i).getNodeId() == nodesId)
            {
                getState().nodes.get(i).setHandlerForClickingTheNode(handler);
                break;
            }
            if(i == getState().nodes.size()-1)
            {
                throw new UnsupportedOperationException(
                        "Graf havent recipe with id= "+nodesId); 
            }
        }
    }
    
    //Добавление обработчика события клика по любой ноде
    public void addHandlerForClickingOnNode(EventListener handler) {
        clickOnNodeListeners.add(handler);
    }
    
    //Вызов соответствующих функций у слушателей события ClickOnNode
    public void notifyEventListeners(ArrayList<EventListener> listeners)
    {
        for(int i=0; i<listeners.size(); i++)
        {
            listeners.get(i).onEventDo();
        }
    }
    
    //Вызов соответствующих функций у слушателей события ClickOnNode по 
    //конкретной ноде
    public void notifyClickOnConcreteNodeEventListeners(String nodesIdClick)
    {
        //Если существует обработчик, который создан для 
        //этой ноды, то вызов его
        for(int i=0; i<getState().nodes.size(); i++)
        {
            if(getState().nodes.get(i).getNodeId() == nodesIdClick)
            {        
                if(getState().nodes.get(i).checkHandlerState())
                {
                    getState().nodes.get(i).onEventClickDo();
                }
                break;
            }
        }
    }
    
    @Override
    public GrafState getState() {
        return (GrafState) super.getState();
    }
}

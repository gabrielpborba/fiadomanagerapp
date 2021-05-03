package com.example.fiadomanager.data.model.client;

import java.util.List;

public class ClientList {

    private List<ClientResponse> clients;

    public ClientList(List<ClientResponse> clients) {
        this.clients = clients;
    }

    public List<ClientResponse> getClientList() {
        return clients;
    }

    public void setClientList(List<ClientResponse> clientList) {
        this.clients = clientList;
    }
}

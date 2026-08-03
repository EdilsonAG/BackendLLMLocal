package com.example.demo.service;

import com.example.demo.service.AssistantTools.TicketInfo;

public class TicketCriadoHolder {
    private static final ThreadLocal<TicketInfo> HOLDER = new ThreadLocal<>();

    public static void set(TicketInfo info) { HOLDER.set(info); }
    public static TicketInfo get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }
}
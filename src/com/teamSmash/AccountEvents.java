package com.teamSmash;

/**
 * Created by branden on 3/4/16 at 19:54.
 */
public class AccountEvents {

    private Account account;
    private Event event;


    public AccountEvents(Account account, Event event) {
        setAccount(account);
        setEvent(event);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
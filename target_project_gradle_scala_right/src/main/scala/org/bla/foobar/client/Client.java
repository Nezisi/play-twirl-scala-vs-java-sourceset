package org.bla.foobar.client;

public class Client {
    public static void main(String[] args) {
        final String finalRole = org.bla.foobar.template.txt.final_role.render("company").toString();
        System.out.println("Final role: " + finalRole);
    }
}

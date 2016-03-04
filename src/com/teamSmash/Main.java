package com.teamSmash;

import spark.Spark;

public class Main {


    public static void main(String[] args) {
        Spark.init();

        Spark.get(
                "/login",
                ((request, response) -> {

                })
        );
    }
}

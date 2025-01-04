package com.miniproject.TEST;

import java.net.URL;

public class ResourceTest {


        public static void main(String[] args) {
            URL resource = ResourceTest.class.getResource("/com/miniproject/images/dashboard-icon.png");
            if (resource != null) {
                System.out.println("Image found: " + resource);
            } else {
                System.err.println("Image not found!");
            }
        }
    }



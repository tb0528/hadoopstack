package com.xiaoxiaomo.hbase.book.ch05.admin;

import org.apache.hadoop.hbase.NamespaceDescriptor;

import java.io.IOException;

// cc NamespaceDescriptorExample Example how to create a NamespaceDescriptor in code
public class NamespaceDescriptorExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        // vv NamespaceDescriptorExample
        NamespaceDescriptor.Builder builder =
                NamespaceDescriptor.create("testspace");
        builder.addConfiguration("key1", "value1");
        NamespaceDescriptor desc = builder.build();
        System.out.println("Namespace: " + desc);
        // ^^ NamespaceDescriptorExample
    }
}

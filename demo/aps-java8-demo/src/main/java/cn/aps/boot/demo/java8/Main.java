package cn.aps.boot.demo.java8;

import com.alibaba.fastjson.JSON;

import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String s = "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n" +
                "             xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n" +
                "             xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\"\n" +
                "             xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\"\n" +
                "             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" id=\"sid-38422fae-e03e-43a3-bef4-bd33b32041b2\"\n" +
                "             targetNamespace=\"http://bpmn.io/bpmn\" exporter=\"bpmn-js (https://demo.bpmn.io)\" exporterVersion=\"5.1.2\">\n" +
                "    <bpmndi:BPMNDiagram id=\"BpmnDiagram_1\">\n" +
                "        <bpmndi:BPMNPlane id=\"BpmnPlane_1\" bpmnElement=\"Process_1\">\n" +
                "            <bpmndi:BPMNShape id=\"TextAnnotation_0emqvso_di\" bpmnElement=\"TextAnnotation_0emqvso\">\n" +
                "                <omgdc:Bounds x=\"340\" y=\"0\" width=\"100\" height=\"30\"/>\n" +
                "            </bpmndi:BPMNShape>\n" +
                "            <bpmndi:BPMNEdge id=\"SequenceFlow_0h21x7r_di\" bpmnElement=\"SequenceFlow_0h21x7r\">\n" +
                "                <omgdi:waypoint x=\"188\" y=\"120\"/>\n" +
                "                <omgdi:waypoint x=\"240\" y=\"120\"/>\n" +
                "            </bpmndi:BPMNEdge>\n" +
                "            <bpmndi:BPMNEdge id=\"Flow_1g7g4s9_di\" bpmnElement=\"Flow_1g7g4s9\">\n" +
                "                <omgdi:waypoint x=\"340\" y=\"120\"/>\n" +
                "                <omgdi:waypoint x=\"392\" y=\"120\"/>\n" +
                "            </bpmndi:BPMNEdge>\n" +
                "        </bpmndi:BPMNPlane>\n" +
                "    </bpmndi:BPMNDiagram>\n" +
                "</definitions>";
        System.out.println(JSON.toJSONString(s));
    }
}
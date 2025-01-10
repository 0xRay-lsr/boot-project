package cn.aps.boot.node;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/12/9 17:08
 */
public class Node implements Serializable {
    @JSONField(ordinal = 1)
    private String id;
    @JSONField(ordinal = 2)
    private String name;
    @JSONField(ordinal = 3)
    private List<Node> children; // 子节点列表

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}

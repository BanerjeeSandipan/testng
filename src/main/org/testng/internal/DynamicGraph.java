package org.testng.internal;

import org.testng.collections.Lists;
import org.testng.internal.annotations.Sets;

import java.util.List;
import java.util.Set;

/**
 * Representation of the graph of methods.
 */
public class DynamicGraph<T extends Comparable> {
  private Set<T> m_nodes = Sets.newHashSet();
  private MapList<T, T> m_dependedUpon = new MapList<T, T>();
  private MapList<T, T> m_dependingOn = new MapList<T, T>();

  public void addNode(T node) {
    m_nodes.add(node);
  }

  public void addEdge(T from, T to) {
    m_nodes.add(from);
    m_nodes.add(to);
    m_dependingOn.put(to, from);
    m_dependedUpon.put(from, to);
  }

  public List<T> getFreeNodes() {
    List<T> result = Lists.newArrayList();
    for (T m : m_nodes) {
      if (!m_dependedUpon.containsKey(m)) {
        result.add(m);
      }
    }
    return result;
  }

  public List<T> getFreeNodesAfterRemoving(T node) {
    remove(node);
    return getFreeNodes();
  }

  public void remove(T node) {
    System.out.println("Removing:" + node + " now:"+ m_dependedUpon);
    m_nodes.remove(node);
    m_dependingOn.remove(node);
    for (T k : m_dependedUpon.getKeys()) {
      List<T> l = m_dependedUpon.get(k);
      boolean f = l.remove(node);
      if (l.size() == 0) m_dependedUpon.remove(k);
    }
    System.out.println(m_dependedUpon);
  }

  public static void main(String[] argv) {
    DynamicGraph mp = new DynamicGraph();
    mp.addEdge("b1", "a1");
    mp.addEdge("b1", "a2");
    mp.addEdge("b2", "a1");
    mp.addEdge("b2", "a2");
    mp.addEdge("c1", "b1");
    mp.addEdge("c1", "b2");
    mp.addNode("x");
    mp.addNode("y");
    System.out.println(mp.getFreeNodes());
    System.out.println(mp.getFreeNodesAfterRemoving("a1"));
    System.out.println(mp.getFreeNodesAfterRemoving("a2"));
    System.out.println(mp.getFreeNodesAfterRemoving("b1"));
    System.out.println(mp.getFreeNodesAfterRemoving("b2"));
    System.out.println(mp.getFreeNodesAfterRemoving("x"));
  }
}
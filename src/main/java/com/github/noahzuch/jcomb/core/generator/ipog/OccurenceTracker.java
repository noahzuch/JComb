/**
 * Copyright 2019 Noah Zuch noahz97@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.noahzuch.jcomb.core.generator.ipog;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

class OccurenceTracker implements IOccurenceTracker {

  private ParameterValueOccurence[] occurences;

  public OccurenceTracker(int[] domainSizes) {
    occurences = new ParameterValueOccurence[domainSizes.length];
    for (int i = 0; i < domainSizes.length; i++) {
      ParameterValueOccurence parameterValueOccurence = new ParameterValueOccurence();
      parameterValueOccurence.occurenceNodes = new OccurenceNode[domainSizes[i]];
      parameterValueOccurence.occurences = new TreeSet<>();
      for (int j = 0; j < domainSizes[i]; j++) {
        OccurenceNode node = new OccurenceNode(j);
        parameterValueOccurence.occurenceNodes[j] = node;
        parameterValueOccurence.occurences.add(node);
      }
      occurences[i] = parameterValueOccurence;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see jcomb.core.ipog.IOccurenceTracker#getLeastFrequentValue(int)
   */
  @Override
  public int getLeastFrequentValue(int parameterIndex) {
    return occurences[parameterIndex].occurences.first().value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see jcomb.core.ipog.IOccurenceTracker#addUsedValue(int, int)
   */
  @Override
  public void addUsedValue(int parameterIndex, int value) {
    ParameterValueOccurence parameterValueOccurence = occurences[parameterIndex];
    OccurenceNode node = parameterValueOccurence.occurenceNodes[value];
    parameterValueOccurence.occurences.remove(node);
    node.occurence++;
    parameterValueOccurence.occurences.add(node);
  }

  public Iterator<Integer> getOccurenceIterator(int parameterIndex) {
    return new ConvertedIterator<OccurenceNode, Integer>(
        occurences[parameterIndex].occurences.iterator(), OccurenceNode::getValue);
  }

  private static class ParameterValueOccurence {

    private OccurenceNode[] occurenceNodes;
    private SortedSet<OccurenceNode> occurences;
  }

  private class OccurenceNode implements Comparable<OccurenceNode> {

    public OccurenceNode(int value) {
      occurence = 0;
      this.value = value;
    }

    private int value;
    private int occurence;

    public int getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + value;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      OccurenceNode other = (OccurenceNode) obj;
      if (value != other.value || occurence != other.occurence) {
        return false;
      }
      return true;
    }

    @Override
    public int compareTo(OccurenceNode o) {
      int compareResult = Integer.compare(occurence, o.occurence);
      if (compareResult == 0) {
        compareResult = Integer.compare(value, o.value);
      }
      return compareResult;
    }
  }

  private class ConvertedIterator<A, B> implements Iterator<B> {

    private Iterator<A> source;
    private Function<A, B> converter;

    public ConvertedIterator(Iterator<A> source, Function<A, B> converter) {
      super();
      this.source = source;
      this.converter = converter;
    }

    @Override
    public boolean hasNext() {
      return source.hasNext();
    }

    @Override
    public B next() {
      return converter.apply(source.next());
    }

  }

}

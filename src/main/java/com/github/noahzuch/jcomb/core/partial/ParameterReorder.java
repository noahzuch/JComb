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
package com.github.noahzuch.jcomb.core.partial;

/**
 * A ParameterReorder saves the reordering and filtering of parameter indices from an existing
 * mapping to a new one.
 * 
 * @author Noah
 *
 */
public class ParameterReorder {

  private Integer[] mappingOldToNewIndizes;
  private int[] mappingNewToOldIndizes;

  /**
   * Creates a new ParameterReorder object for the given mappings.
   * 
   * @param mappingNewToOldIndizes A mapping of the new parameter indices to the old ones.
   * @param oldParamterCount The total number of old parameters.
   */
  public ParameterReorder(int[] mappingNewToOldIndizes, int oldParamterCount) {
    this.mappingNewToOldIndizes = mappingNewToOldIndizes;
    mappingOldToNewIndizes = new Integer[oldParamterCount];
    for (int newIndex = 0; newIndex < mappingNewToOldIndizes.length; newIndex++) {
      int oldIndex = mappingNewToOldIndizes[newIndex];
      if (oldIndex >= 0 && oldIndex < oldParamterCount) {
        mappingOldToNewIndizes[oldIndex] = newIndex;
      } else {
        throw new IllegalArgumentException(
            "Given mapping contains the undefined old parameter: " + oldIndex);
      }
    }
  }

  /**
   * Checks if the given index is used as a new parameter index in this reorder.
   * @param newIndex The index to check.
   * @return Whether or not the index is a new parameter index.
   */
  public boolean isLegalNewParam(int newIndex) {
    return newIndex >= 0 && newIndex < mappingNewToOldIndizes.length;
  }

  /**
   * Checks if the given index is used as am old parameter index in this reorder.
   * @param oldIndex The index to check.
   * @return Whether or not the index is an old parameter index.
   */
  public boolean isLegalOldParam(int oldIndex) {
    return oldIndex >= 0 && oldIndex < mappingOldToNewIndizes.length;
  }

  /**
   * Checks if this ParameterReorder contains a reoder for the given old paramter index.
   * @param oldIndex The old index to check.
   * @return Whether or not the old index is reordered by this ParameterReorder.
   */
  public boolean containsReorderForOldParam(int oldIndex) {
    return isLegalOldParam(oldIndex) && mappingOldToNewIndizes[oldIndex] != null;
  }

  /**
   * Converts an old parameter index to its new index.
   * 
   * @param oldIndex The old parameter index to convert.
   * @return The new parameter index.
   */
  public int getNewParamIndexFromOld(int oldIndex) {
    if (!containsReorderForOldParam(oldIndex)) {
      throw new IllegalArgumentException(
          "Given old Parameter '" + oldIndex + "' is not reordered in this ParameterReorder");
    }
    return mappingOldToNewIndizes[oldIndex];
  }

  /**
   * Converts a new parameter index to its old index.
   * 
   * @param newIndex The new parameter index to convert.
   * @return The old index of the parameter.
   */
  public int getOldParamIndexFromNew(int newIndex) {
    if (!isLegalNewParam(newIndex)) {
      throw new IllegalArgumentException(
          "Given Index '" + newIndex + "' does not exist in the current reordering");
    }
    return mappingNewToOldIndizes[newIndex];
  }

  /**
   * Returns the number of reorders in this ParameterReorder.
   * @return The number of reorders in this ParameterReorder
   */
  public int getReorderedParameterCount() {
    return mappingNewToOldIndizes.length;
  }

}

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

package com.github.noahzuch.jcomb.core;

/**
 * A general exception class, that is used if some problem is found with the current test setup.
 * 
 * @author Noah Zuch
 *
 */
public class JCombException extends RuntimeException {

  private static final long serialVersionUID = -8987333414344667571L;

  /**
   * Creates a new exception object.
   */
  public JCombException() {
    super();
  }

  /**
   * Creates a new exception object.
   * 
   * @param message The message for this exception.
   */
  public JCombException(String message) {
    // super(message);
    super("JComb encountered a problem with the current test class:\n" + message);
  }

  /**
   * Creates a new exception object.
   * 
   * @param cause The cause of this exception
   */
  public JCombException(Throwable cause) {
    super("JComb encountered a problem with the current test class", cause);

  }

  /**
   * Creates a new exception object.
   * 
   * @param cause The cause of this exception
   */
  public JCombException(String message, Throwable cause) {
    super("JComb encountered a problem with the current test class:\n" + message, cause);

  }

}

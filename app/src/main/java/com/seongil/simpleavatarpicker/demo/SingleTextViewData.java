/*
 * Copyright (C) 2017 Seong-il Kim <kims172@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seongil.simpleavatarpicker.demo;

import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class SingleTextViewData implements RecyclerViewItem {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private String text;

    // ========================================================================
    // constructors
    // ========================================================================
    public SingleTextViewData(String text) {
        this.text = text;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}

/*
 * Copyright (c) 2016 Dimension Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.director.dimensiondata.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class Dates {

  private static final DateTimeFormatter DATE_TIME_FORMATTER_ISO8601 = ISODateTimeFormat.dateTime();

  public static Date getDateFromTimestamp(String timestamp) {
    if (timestamp != null && !timestamp.isEmpty()) {
      return DATE_TIME_FORMATTER_ISO8601.parseDateTime(timestamp).toDate();
    }

    return null;
  }
}

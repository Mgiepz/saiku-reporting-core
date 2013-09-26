/*******************************************************************************
 * Copyright 2013 Marius Giepz and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
@XmlSchema(
namespace = "http://www.analytical-labs.com/namespaces/saiku-reporting/1.0", 
elementFormDefault = XmlNsForm.QUALIFIED,
xmlns={@XmlNs(prefix="saiku", 
namespaceURI="http://www.analytical-labs.com/namespaces/saiku-reporting/1.0")})  

package org.saiku.reporting.core.model;
import javax.xml.bind.annotation.*;

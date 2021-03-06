/**
 * Copyright 2010-2015 Axel Fontaine
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.info.validation;

/**
 * Strict validation check of each migration step.
 *
 * Following migration validation will be done
 * <ul>
 *     <li>{@link MissingClasspathValidation}</li>
 *     <li>{@link NotAppliedMigrationValidation}</li>
 *     <li>{@link MigrationTypeValidation}</li>
 *     <li>{@link MigrationChecksumValidation}</li>
 *     <li>{@link MigrationDescriptionValidation}</li>
 * </ul>
 */
public class StrictValidationStrategy extends ContainerValidationStrategy {

    public StrictValidationStrategy() {
        super(new MissingClasspathValidation(),
                new NotAppliedMigrationValidation(),
                new MigrationTypeValidation(),
                new MigrationChecksumValidation(),
                new MigrationDescriptionValidation());
    }


}

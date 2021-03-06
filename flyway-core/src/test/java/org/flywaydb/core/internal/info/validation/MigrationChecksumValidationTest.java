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

import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.info.MigrationInfoContext;
import org.flywaydb.core.internal.info.MigrationInfoData;
import org.flywaydb.core.internal.metadatatable.AppliedMigration;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MigrationChecksumValidationTest {
    @Test
    public void testValidationFailedNoError() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);

        boolean validationError = migrationChecksumValidation.validationFailed(migrationInfoDataMock);

        assertFalse("validation not passed", validationError);
    }

    @Test
    public void testValidationFailedNoAppliedMigration() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        when(migrationInfoDataMock.getAppliedMigration()).thenReturn(null);

        boolean validationError = migrationChecksumValidation.validationFailed(migrationInfoDataMock);

        assertFalse("validation not passed", validationError);
    }

    @Test
    public void testValidationFailedNoResolvedMigration() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        when(migrationInfoDataMock.getResolvedMigration()).thenReturn(null);

        boolean validationError = migrationChecksumValidation.validationFailed(migrationInfoDataMock);

        assertFalse("validation not passed", validationError);
    }

    @Test
    public void testValidationFailedVersionChecksumEqual() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        migrationInfoDataMock.getMigrationInfoContext().baseline = MigrationVersion.CURRENT;

        boolean validationError = migrationChecksumValidation.validationFailed(migrationInfoDataMock);

        assertFalse("validation not passed", validationError);
    }

    @Test
    public void testValidationFailedVersionChecksumDifferent() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        migrationInfoDataMock.getMigrationInfoContext().baseline = MigrationVersion.CURRENT;
        when(migrationInfoDataMock.getAppliedMigration().getChecksum()).thenReturn(84);

        boolean validationError = migrationChecksumValidation.validationFailed(migrationInfoDataMock);

        assertTrue("validation not passed", validationError);
    }


    @Test
    public void testGetValidationErrorNoError() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);

        String validationErrorText = migrationChecksumValidation.getValidationError(migrationInfoDataMock);

        assertNull("wrong validation error detected", validationErrorText);
    }

    @Test
    public void testGetValidationErrorMigration() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        when(migrationInfoDataMock.getAppliedMigration()).thenReturn(null);

        String validationErrorText = migrationChecksumValidation.getValidationError(migrationInfoDataMock);

        assertNull("wrong validation error detected", validationErrorText);
    }

    @Test
    public void testGetValidationErrorNoResolvedMigration() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        when(migrationInfoDataMock.getResolvedMigration()).thenReturn(null);

        String validationErrorText = migrationChecksumValidation.getValidationError(migrationInfoDataMock);

        assertNull("wrong validation error detected", validationErrorText);
    }

    @Test
    public void testGetValidationErrorFailedVersionChecksumEqual() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        migrationInfoDataMock.getMigrationInfoContext().baseline = MigrationVersion.CURRENT;

        String validationErrorText = migrationChecksumValidation.getValidationError(migrationInfoDataMock);

        assertNull("wrong validation error detected", validationErrorText);
    }

    @Test
    public void testGetValidationErrorFailedVersionChecksumDifferent() {
        MigrationChecksumValidation migrationChecksumValidation = new MigrationChecksumValidation();
        MigrationInfoData migrationInfoDataMock = createMigrationInfoMockForValidation(MigrationVersion.LATEST, 42);
        migrationInfoDataMock.getMigrationInfoContext().baseline = MigrationVersion.CURRENT;
        when(migrationInfoDataMock.getAppliedMigration().getChecksum()).thenReturn(84);

        String validationErrorText = migrationChecksumValidation.getValidationError(migrationInfoDataMock);

        assertNotNull("Validation error not detected", validationErrorText);
        assertFalse("Error message must contain a string", validationErrorText.trim().isEmpty());
    }

    private MigrationInfoData createMigrationInfoMockForValidation(MigrationVersion migrationVersion, Integer checksum) {
        MigrationInfoData migrationInfoDataMock = mock(MigrationInfoData.class);

        AppliedMigration appliedMigrationMock = mock(AppliedMigration.class);
        ResolvedMigration resolvedMigrationMock = mock(ResolvedMigration.class);

        MigrationInfoContext migrationInfoContext = new MigrationInfoContext();
        migrationInfoContext.baseline = migrationVersion;

        when(migrationInfoDataMock.getMigrationInfoContext()).thenReturn(migrationInfoContext);
        when(migrationInfoDataMock.getResolvedMigration()).thenReturn(resolvedMigrationMock);
        when(migrationInfoDataMock.getAppliedMigration()).thenReturn(appliedMigrationMock);

        when(appliedMigrationMock.getChecksum()).thenReturn(checksum);
        when(resolvedMigrationMock.getChecksum()).thenReturn(checksum);

        when(migrationInfoDataMock.getVersion()).thenReturn(migrationVersion);

        return migrationInfoDataMock;
    }
}

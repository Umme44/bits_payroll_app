import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IMPORT_DATA_ROUTE, ImportDataComponent } from './index';
import { ImportNomineeImageComponent } from 'app/admin/import-data/import-nominee-image.component';
import { ImportInsuranceImageComponent } from 'app/admin/import-data/import-insurance-image.component';
import { ImportEmployeeImageComponent } from 'app/admin/import-data/import-employee-image.component';
import { ImportEmployeeImageUploadComponent } from 'app/admin/import-data/import-employee-image-upload.component';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from 'app/shared/search-text-box/search-text-box.module';
import { ImportEmployeeImageUpdateComponent } from 'app/admin/import-data/import-employee-image-update.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(IMPORT_DATA_ROUTE), BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    ImportDataComponent,
    ImportNomineeImageComponent,
    ImportInsuranceImageComponent,
    ImportEmployeeImageComponent,
    ImportEmployeeImageUploadComponent,
    ImportEmployeeImageUpdateComponent,
  ],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppImportDataModule {}

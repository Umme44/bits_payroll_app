import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeStaticFileComponent } from './list/employee-static-file.component';
import { EmployeeStaticFileDetailComponent } from './detail/employee-static-file-detail.component';
import { EmployeeStaticFileUpdateComponent } from './update/employee-static-file-update.component';
import { EmployeeStaticFileDeleteDialogComponent } from './delete/employee-static-file-delete-dialog.component';
import { EmployeeStaticFileRoutingModule } from './route/employee-static-file-routing.module';
import { EmployeeIdCardUploadComponent } from './employee-id-card-upload/employee-id-card-upload.component';
import { EmployeeIdCardListComponent } from './employee-id-card-upload/employee-id-card-list.component';
import { EmployeeIdCardUploadUpdateComponent } from './employee-id-card-upload/employee-id-card-upload-update.component';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [SharedModule, EmployeeStaticFileRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    EmployeeStaticFileComponent,
    EmployeeStaticFileDetailComponent,
    EmployeeStaticFileUpdateComponent,
    EmployeeStaticFileDeleteDialogComponent,
    EmployeeIdCardUploadComponent,
    EmployeeIdCardListComponent,
    EmployeeIdCardUploadUpdateComponent,
  ],
})
export class EmployeeStaticFileModule {}

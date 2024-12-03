import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeDocumentComponent } from './list/employee-document.component';
import { EmployeeDocumentDetailComponent } from './detail/employee-document-detail.component';
import { EmployeeDocumentUpdateComponent } from './update/employee-document-update.component';
import { EmployeeDocumentDeleteDialogComponent } from './delete/employee-document-delete-dialog.component';
import { EmployeeDocumentRoutingModule } from './route/employee-document-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeDocumentRoutingModule],
  declarations: [
    EmployeeDocumentComponent,
    EmployeeDocumentDetailComponent,
    EmployeeDocumentUpdateComponent,
    EmployeeDocumentDeleteDialogComponent,
  ],
})
export class EmployeeDocumentModule {}

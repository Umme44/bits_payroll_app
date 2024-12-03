import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeNOCComponent } from './list/employee-noc.component';
import { EmployeeNOCDetailComponent } from './detail/employee-noc-detail.component';
import { EmployeeNOCUpdateComponent } from './update/employee-noc-update.component';
import { EmployeeNOCDeleteDialogComponent } from './delete/employee-noc-delete-dialog.component';
import { EmployeeNOCRoutingModule } from './route/employee-noc-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeNOCRoutingModule],
  declarations: [EmployeeNOCComponent, EmployeeNOCDetailComponent, EmployeeNOCUpdateComponent, EmployeeNOCDeleteDialogComponent],
})
export class EmployeeNOCModule {}

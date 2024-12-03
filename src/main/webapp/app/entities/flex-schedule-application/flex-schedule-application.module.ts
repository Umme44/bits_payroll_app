import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { FlexScheduleApplicationComponent } from './list/flex-schedule-application.component';
import { FlexScheduleApplicationDetailComponent } from './detail/flex-schedule-application-detail.component';
import { FlexScheduleApplicationUpdateComponent } from './update/flex-schedule-application-update.component';
import { FlexScheduleApplicationDeleteDialogComponent } from './delete/flex-schedule-application-delete-dialog.component';
import { FlexScheduleApplicationRoutingModule } from './route/flex-schedule-application-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollShowStatusModule } from '../../shared/show-status/show-stauts.module';

@NgModule({
  imports: [
    SharedModule,
    NgSelectModule,
    FlexScheduleApplicationRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollShowStatusModule,
  ],
  declarations: [
    FlexScheduleApplicationComponent,
    FlexScheduleApplicationDetailComponent,
    FlexScheduleApplicationUpdateComponent,
    FlexScheduleApplicationDeleteDialogComponent,
  ],
})
export class FlexScheduleApplicationModule {}

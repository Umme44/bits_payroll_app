import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FlexScheduleComponent } from './list/flex-schedule.component';
import { FlexScheduleRoutingModule } from './route/flex-schedule-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSelectEmployeeFormModule} from "../../shared/select-employee-form/select-employee-form.module";

@NgModule({
  imports: [SharedModule, FlexScheduleRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [
    FlexScheduleComponent,
    // FlexScheduleDetailComponent,
    // FlexScheduleUpdateComponent,
    // FlexScheduleDeleteDialogComponent
  ],
})

export class FlexScheduleModule {}

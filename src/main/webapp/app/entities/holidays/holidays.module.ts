import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HolidaysComponent } from './list/holidays.component';
import { HolidaysDetailComponent } from './detail/holidays-detail.component';
import { HolidaysUpdateComponent } from './update/holidays-update.component';
import { HolidaysDeleteDialogComponent } from './delete/holidays-delete-dialog.component';
import { HolidaysRoutingModule } from './route/holidays-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSelectYearModule} from "../../shared/select-year/select-year.module";

@NgModule({
  imports: [SharedModule, HolidaysRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectYearModule],
  declarations: [HolidaysComponent, HolidaysDetailComponent, HolidaysUpdateComponent, HolidaysDeleteDialogComponent],
})
export class HolidaysModule {}

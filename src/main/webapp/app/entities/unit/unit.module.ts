import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UnitComponent } from './list/unit.component';
import { UnitDetailComponent } from './detail/unit-detail.component';
import { UnitUpdateComponent } from './update/unit-update.component';
import { UnitDeleteDialogComponent } from './delete/unit-delete-dialog.component';
import { UnitRoutingModule } from './route/unit-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, UnitRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [UnitComponent, UnitDetailComponent, UnitUpdateComponent, UnitDeleteDialogComponent],
})
export class UnitModule {}

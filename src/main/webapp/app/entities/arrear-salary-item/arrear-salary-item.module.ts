import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArrearSalaryItemComponent } from './list/arrear-salary-item.component';
import { ArrearSalaryItemDetailComponent } from './detail/arrear-salary-item-detail.component';
import { ArrearSalaryItemUpdateComponent } from './update/arrear-salary-item-update.component';
import { ArrearSalaryItemDeleteDialogComponent } from './delete/arrear-salary-item-delete-dialog.component';
import { ArrearSalaryItemRoutingModule } from './route/arrear-salary-item-routing.module';

@NgModule({
  imports: [SharedModule, ArrearSalaryItemRoutingModule],
  declarations: [
    ArrearSalaryItemComponent,
    ArrearSalaryItemDetailComponent,
    ArrearSalaryItemUpdateComponent,
    ArrearSalaryItemDeleteDialogComponent,
  ],
})
export class ArrearSalaryItemModule {}

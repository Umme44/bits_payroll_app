import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProRataFestivalBonusComponent } from './list/pro-rata-festival-bonus.component';
import { ProRataFestivalBonusDetailComponent } from './detail/pro-rata-festival-bonus-detail.component';
import { ProRataFestivalBonusUpdateComponent } from './update/pro-rata-festival-bonus-update.component';
import { ProRataFestivalBonusDeleteDialogComponent } from './delete/pro-rata-festival-bonus-delete-dialog.component';
import { ProRataFestivalBonusRoutingModule } from './route/pro-rata-festival-bonus-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';

@NgModule({
  imports: [SharedModule, ProRataFestivalBonusRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [
    ProRataFestivalBonusComponent,
    ProRataFestivalBonusDetailComponent,
    ProRataFestivalBonusUpdateComponent,
    ProRataFestivalBonusDeleteDialogComponent,
  ],
})
export class ProRataFestivalBonusModule {}

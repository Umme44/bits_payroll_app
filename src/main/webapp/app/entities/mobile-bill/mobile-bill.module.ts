import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MobileBillComponent } from './list/mobile-bill.component';
import { MobileBillDetailComponent } from './detail/mobile-bill-detail.component';
import { MobileBillUpdateComponent } from './update/mobile-bill-update.component';
import { MobileBillDeleteDialogComponent } from './delete/mobile-bill-delete-dialog.component';
import { MobileBillRoutingModule } from './route/mobile-bill-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, MobileBillRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    MobileBillComponent,
    MobileBillDetailComponent,
    MobileBillUpdateComponent,
    MobileBillDeleteDialogComponent
  ],
})
export class MobileBillModule {}

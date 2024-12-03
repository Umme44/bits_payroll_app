import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AitConfigComponent } from './list/ait-config.component';
import { AitConfigDetailComponent } from './detail/ait-config-detail.component';
import { AitConfigUpdateComponent } from './update/ait-config-update.component';
import { AitConfigDeleteDialogComponent } from './delete/ait-config-delete-dialog.component';
import { AitConfigRoutingModule } from './route/ait-config-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, AitConfigRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [AitConfigComponent, AitConfigDetailComponent, AitConfigUpdateComponent, AitConfigDeleteDialogComponent],
})
export class AitConfigModule {}

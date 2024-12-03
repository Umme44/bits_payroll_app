import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConfigComponent } from './list/config.component';
import { ConfigDetailComponent } from './detail/config-detail.component';
import { ConfigUpdateComponent } from './update/config-update.component';
import { ConfigDeleteDialogComponent } from './delete/config-delete-dialog.component';
import { ConfigRoutingModule } from './route/config-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, ConfigRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [ConfigComponent, ConfigDetailComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent],
})
export class ConfigModule {}

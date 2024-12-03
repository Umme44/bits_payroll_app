import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { ConfigCustomUpdateComponent } from './config-custom-update.component';
import { configCustomRoute } from './config-custom.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgIf } from '@angular/common';

@NgModule({
  imports: [RouterModule.forChild(configCustomRoute), BitsHrPayrollHeaderModule, NgIf, SharedModule],
  declarations: [ConfigCustomUpdateComponent],
  entryComponents: [],
})
export class ConfigCustomModule {}

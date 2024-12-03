import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FestivalBonusConfigComponent } from './list/festival-bonus-config.component';
import { FestivalBonusConfigDetailComponent } from './detail/festival-bonus-config-detail.component';
import { FestivalBonusConfigUpdateComponent } from './update/festival-bonus-config-update.component';
import { FestivalBonusConfigDeleteDialogComponent } from './delete/festival-bonus-config-delete-dialog.component';
import { FestivalBonusConfigRoutingModule } from './route/festival-bonus-config-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, FestivalBonusConfigRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    FestivalBonusConfigComponent,
    FestivalBonusConfigDetailComponent,
    FestivalBonusConfigUpdateComponent,
    FestivalBonusConfigDeleteDialogComponent,
  ],
})
export class FestivalBonusConfigModule {}

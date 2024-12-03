import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FestivalComponent } from './list/festival.component';
import { FestivalDetailComponent } from './detail/festival-detail.component';
import { FestivalUpdateComponent } from './update/festival-update.component';
import { FestivalDeleteDialogComponent } from './delete/festival-delete-dialog.component';
import { FestivalRoutingModule } from './route/festival-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {FestivalBonusDetailsComponent} from "./bonus-details/festival-bonus-details.component";
import {BitsHrPayrollSearchTextBoxModule} from "../../shared/search-text-box/search-text-box.module";

@NgModule({
  imports: [SharedModule, FestivalRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [FestivalComponent, FestivalDetailComponent, FestivalUpdateComponent, FestivalDeleteDialogComponent,FestivalBonusDetailsComponent],
})
export class FestivalModule {}

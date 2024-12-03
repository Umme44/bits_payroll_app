import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FestivalBonusDetailsComponent } from './list/festival-bonus-details.component';
import { FestivalBonusDetailsDetailComponent } from './detail/festival-bonus-details-detail.component';
import { FestivalBonusDetailsUpdateComponent } from './update/festival-bonus-details-update.component';
import { FestivalBonusDetailsDeleteDialogComponent } from './delete/festival-bonus-details-delete-dialog.component';
import { FestivalBonusDetailsRoutingModule } from './route/festival-bonus-details-routing.module';

@NgModule({
  imports: [SharedModule, FestivalBonusDetailsRoutingModule],
  declarations: [
    FestivalBonusDetailsComponent,
    FestivalBonusDetailsDetailComponent,
    FestivalBonusDetailsUpdateComponent,
    FestivalBonusDetailsDeleteDialogComponent,
  ],
})
export class FestivalBonusDetailsModule {}

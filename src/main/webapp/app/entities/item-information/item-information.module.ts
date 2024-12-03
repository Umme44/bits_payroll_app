import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemInformationComponent } from './list/item-information.component';
import { ItemInformationDetailComponent } from './detail/item-information-detail.component';
import { ItemInformationUpdateComponent } from './update/item-information-update.component';
import { ItemInformationDeleteDialogComponent } from './delete/item-information-delete-dialog.component';
import { ItemInformationRoutingModule } from './route/item-information-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, ItemInformationRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    ItemInformationComponent,
    ItemInformationDetailComponent,
    ItemInformationUpdateComponent,
    ItemInformationDeleteDialogComponent,
  ],
})
export class ItemInformationModule {}

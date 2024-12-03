import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OfficeNoticesComponent } from './list/office-notices.component';
import { OfficeNoticesDetailComponent } from './detail/office-notices-detail.component';
import { OfficeNoticesUpdateComponent } from './update/office-notices-update.component';
import { OfficeNoticesDeleteDialogComponent } from './delete/office-notices-delete-dialog.component';
import { OfficeNoticesRoutingModule } from './route/office-notices-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, OfficeNoticesRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [OfficeNoticesComponent, OfficeNoticesDetailComponent, OfficeNoticesUpdateComponent, OfficeNoticesDeleteDialogComponent],
})
export class OfficeNoticesModule {}

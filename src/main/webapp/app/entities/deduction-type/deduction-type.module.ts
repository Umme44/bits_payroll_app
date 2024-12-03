import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeductionTypeComponent } from './list/deduction-type.component';
import { DeductionTypeDetailComponent } from './detail/deduction-type-detail.component';
import { DeductionTypeUpdateComponent } from './update/deduction-type-update.component';
import { DeductionTypeDeleteDialogComponent } from './delete/deduction-type-delete-dialog.component';
import { DeductionTypeRoutingModule } from './route/deduction-type-routing.module';

@NgModule({
    imports: [SharedModule, DeductionTypeRoutingModule],
    declarations: [DeductionTypeComponent, DeductionTypeDetailComponent, DeductionTypeUpdateComponent, DeductionTypeDeleteDialogComponent],
    exports: [
        DeductionTypeComponent
    ]
})
export class DeductionTypeModule {}

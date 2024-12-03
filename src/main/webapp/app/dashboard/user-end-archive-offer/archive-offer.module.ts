import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { archiveOfferRoute } from './archive-offer.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import {SharedModule} from "../../shared/shared.module";
import {ArchiveOfferComponent} from "./archive-offer.component";

@NgModule({
  imports: [SharedModule, RouterModule.forChild(archiveOfferRoute), BitsHrPayrollHeaderModule],
  declarations: [ArchiveOfferComponent],
})
export class BitsHrPayrollArchiveOfferModule {}

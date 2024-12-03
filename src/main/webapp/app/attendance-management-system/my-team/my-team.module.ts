import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {MY_TEAM_ROUTE} from "./my-team.route";
import {MyTeamComponent} from "./my-team.component";
import {SharedModule} from "../../shared/shared.module";
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSearchTextBoxModule} from "../../shared/search-text-box/search-text-box.module";

@NgModule({
  imports: [SharedModule, RouterModule.forChild(MY_TEAM_ROUTE), BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    MyTeamComponent,
  ],

})
export class BitsHrPayrollMyTeamModule {}

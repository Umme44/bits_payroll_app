import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {IHoldFbDisbursement} from "../hold-fb-disbursement.model";



@Component({
  selector: 'jhi-hold-fb-disbursement-detail',
  templateUrl: './hold-fb-disbursement-detail.component.html',
})
export class HoldFbDisbursementDetailComponent implements OnInit {
  holdFbDisbursement: IHoldFbDisbursement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holdFbDisbursement }) => (this.holdFbDisbursement = holdFbDisbursement));
  }

  previousState(): void {
    window.history.back();
  }
}



/*
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';

@Component({
  selector: 'jhi-hold-fb-disbursement-detail',
  templateUrl: './hold-fb-disbursement-detail.component.html',
})
export class HoldFbDisbursementDetailComponent implements OnInit {
  holdFbDisbursement: IHoldFbDisbursement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holdFbDisbursement }) => {
      this.holdFbDisbursement = holdFbDisbursement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
*/

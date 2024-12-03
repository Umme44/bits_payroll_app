import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomType } from '../room-type.model';

@Component({
  selector: 'jhi-room-type-detail',
  templateUrl: './room-type-detail.component.html',
})
export class RoomTypeDetailComponent implements OnInit {
  roomType: IRoomType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomType }) => {
      this.roomType = roomType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

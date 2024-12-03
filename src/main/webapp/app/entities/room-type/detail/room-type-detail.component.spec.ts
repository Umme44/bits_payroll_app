import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomTypeDetailComponent } from './room-type-detail.component';

describe('RoomType Management Detail Component', () => {
  let comp: RoomTypeDetailComponent;
  let fixture: ComponentFixture<RoomTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoomTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ roomType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RoomTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RoomTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load roomType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.roomType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

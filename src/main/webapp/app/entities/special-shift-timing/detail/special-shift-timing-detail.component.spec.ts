import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpecialShiftTimingDetailComponent } from './special-shift-timing-detail.component';

describe('SpecialShiftTiming Management Detail Component', () => {
  let comp: SpecialShiftTimingDetailComponent;
  let fixture: ComponentFixture<SpecialShiftTimingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpecialShiftTimingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ specialShiftTiming: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SpecialShiftTimingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpecialShiftTimingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load specialShiftTiming on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.specialShiftTiming).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

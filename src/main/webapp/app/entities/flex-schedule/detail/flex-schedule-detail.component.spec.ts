import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FlexScheduleDetailComponent } from './flex-schedule-detail.component';

describe('FlexSchedule Management Detail Component', () => {
  let comp: FlexScheduleDetailComponent;
  let fixture: ComponentFixture<FlexScheduleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FlexScheduleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ flexSchedule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FlexScheduleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FlexScheduleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load flexSchedule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.flexSchedule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

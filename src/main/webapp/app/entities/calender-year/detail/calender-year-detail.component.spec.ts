import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CalenderYearDetailComponent } from './calender-year-detail.component';

describe('CalenderYear Management Detail Component', () => {
  let comp: CalenderYearDetailComponent;
  let fixture: ComponentFixture<CalenderYearDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CalenderYearDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ calenderYear: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CalenderYearDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CalenderYearDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load calenderYear on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.calenderYear).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

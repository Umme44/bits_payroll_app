import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HolidaysDetailComponent } from './holidays-detail.component';

describe('Holidays Management Detail Component', () => {
  let comp: HolidaysDetailComponent;
  let fixture: ComponentFixture<HolidaysDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HolidaysDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ holidays: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HolidaysDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HolidaysDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load holidays on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.holidays).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FestivalBonusDetailsDetailComponent } from './festival-bonus-details-detail.component';

describe('FestivalBonusDetails Management Detail Component', () => {
  let comp: FestivalBonusDetailsDetailComponent;
  let fixture: ComponentFixture<FestivalBonusDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FestivalBonusDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ festivalBonusDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FestivalBonusDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FestivalBonusDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load festivalBonusDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.festivalBonusDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

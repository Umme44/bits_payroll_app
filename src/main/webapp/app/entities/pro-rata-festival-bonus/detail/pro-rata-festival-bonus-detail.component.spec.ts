import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProRataFestivalBonusDetailComponent } from './pro-rata-festival-bonus-detail.component';

describe('ProRataFestivalBonus Management Detail Component', () => {
  let comp: ProRataFestivalBonusDetailComponent;
  let fixture: ComponentFixture<ProRataFestivalBonusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProRataFestivalBonusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ proRataFestivalBonus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProRataFestivalBonusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProRataFestivalBonusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load proRataFestivalBonus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.proRataFestivalBonus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

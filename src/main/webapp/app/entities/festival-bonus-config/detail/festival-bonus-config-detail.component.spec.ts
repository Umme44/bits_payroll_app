import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FestivalBonusConfigDetailComponent } from './festival-bonus-config-detail.component';

describe('FestivalBonusConfig Management Detail Component', () => {
  let comp: FestivalBonusConfigDetailComponent;
  let fixture: ComponentFixture<FestivalBonusConfigDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FestivalBonusConfigDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ festivalBonusConfig: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FestivalBonusConfigDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FestivalBonusConfigDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load festivalBonusConfig on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.festivalBonusConfig).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

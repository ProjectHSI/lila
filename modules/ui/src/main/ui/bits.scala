package lila.ui

import ScalatagsTemplate.{ *, given }
import chess.format.Fen
import lila.core.i18n.Translate
import lila.core.security.HcaptchaForm

object bits:

  val engineFullName = "Stockfish 16.1"

  def subnav(mods: Modifier*) = st.aside(cls := "subnav"):
    st.nav(cls := "subnav__inner")(mods)

  def pageMenuSubnav(mods: Modifier*) = subnav(cls := "page-menu__menu", mods)

  def mselect(id: String, current: Frag, items: Seq[Tag]) =
    div(cls := "mselect")(
      input(
        tpe          := "checkbox",
        cls          := "mselect__toggle fullscreen-toggle",
        st.id        := s"mselect-$id",
        autocomplete := "off"
      ),
      label(`for` := s"mselect-$id", cls := "mselect__label")(current),
      label(`for` := s"mselect-$id", cls := "fullscreen-mask"),
      st.nav(cls := "mselect__list")(items.map(_(cls := "mselect__item")))
    )

  def fenAnalysisLink(fen: Fen.Full)(using Translate) =
    a(href := s"/analysis/${ChessHelper.underscoreFen(fen)}")(lila.core.i18n.I18nKey.site.analysis())

  private val dataSitekey = attr("data-sitekey")

  def hcaptcha(form: HcaptchaForm[?]) =
    div(cls := "h-captcha form-group", dataSitekey := form.config.key)
